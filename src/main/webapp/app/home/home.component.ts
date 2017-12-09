import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Account, LoginModalService, Principal } from '../shared';
import {RfbLocationService} from '../entities/rfb-location/rfb-location.service';
import {ResponseWrapper} from '../shared/model/response-wrapper.model';
import {RfbLocation} from '../entities/rfb-location/rfb-location.model';
import {RfbEventService} from '../entities/rfb-event/rfb-event.service';
import {AccountService} from '../shared/auth/account.service';
import {RfbEvent} from '../entities/rfb-event/rfb-event.model';
import {User} from '../shared/user/user.model';
import {RfbEventAttendance} from '../entities/rfb-event-attendance/rfb-event-attendance.model';
import {RfbEventAttendanceService} from '../entities/rfb-event-attendance/rfb-event-attendance.service';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: [
        'home.css'
    ]

})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    isSaving: boolean;
    locations: RfbLocation[];
    todaysEvent: RfbEvent;
    currentUser: User;
    model: any;
    rfbEventAttendance: RfbEventAttendance;
    errors: any = {invalidEventCode: ''};
    checkedIn = false;

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private locationService: RfbLocationService,
        private eventService: RfbEventService,
        private accountService: AccountService,
        private rfbEventAttendanceService: RfbEventAttendanceService
    ) {
    }

    ngOnInit() {
        this.model = {location: 0, eventCode: ''};
        this.rfbEventAttendance = new RfbEventAttendance(null, new Date(), new RfbEvent(), new User());
        // for whatever reason this.principal.isAuthenticated() never returns true. This is my way of making
        // sure this isn't run when nobody is logged in.
        if (this.principal.hasAnyAuthorityDirect(['ROLE_RUNNER', 'ROLE_ORGANIZER', 'ROLE_ADMIN'])) {
            this.principal.identity().then((account) => {
                this.account = account;
                this.afterUserAccountSetup(account);
            });
        }
        this.registerAuthenticationSuccess();
        this.loadLocations();
    }

    afterUserAccountSetup(user: Account) {
        this.currentUser = user;
        this.rfbEventAttendance.userDTO = user;
        // we can set todays event for anyone who has a homeLocation. If they don't we should setTodays event
        // when they change the location drop down || or just grab the event and then compare their event code to the events
        if (this.currentUser.authorities.indexOf('ROLE_ORGANIZER') !== -1) {
            this.setTodaysEvent(this.currentUser.homeLocation);
        }
        if (this.currentUser.authorities.indexOf('ROLE_RUNNER') !== -1) {
            // set home location
            this.model.location = this.currentUser.homeLocation;
        }
    }

    setTodaysEvent(locationID: number) {
        // reach out to our event service and find an event with todays date & this location id: homeLocationId;
        this.eventService.findByLocation(locationID).subscribe( (rfbEvent: RfbEvent) => {
            this.todaysEvent = rfbEvent;
        });
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', (message) => {
            this.principal.identity().then((account) => {
                this.account = account;
                this.afterUserAccountSetup(account);
            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    loadLocations() {
        this.locationService.query({
            page: 0,
            size: 100,
            sort: ['locationName', 'ASC']}).subscribe(
            (res: ResponseWrapper) => {
                this.locations = res.json;
            },
            (res: ResponseWrapper) => { console.log(res) }
        );
    }

    checkIn() {
        this.eventService.findByLocation(this.model.location).subscribe( (rfbEvent: RfbEvent) => {
            const thisEvent = rfbEvent;
            this.rfbEventAttendance.rfbEventDTO = rfbEvent;
            if (thisEvent.eventCode === this.model.eventCode) {
                // you are checked in
                this.rfbEventAttendanceService.create(this.rfbEventAttendance).subscribe( (userCheckedIn: RfbEventAttendance) => {
                    this.checkedIn = true;
                });
            } else {
                this.errors.invalidEventCode = 'There is either no run today for this location or you have entered an incorrect event code. Please try again.';
            }

        });
    }

    clear() {}

    setHomeLocation() {}

}
