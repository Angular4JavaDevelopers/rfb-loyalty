import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Account, LoginModalService, Principal } from '../shared';
import {RfbLocationService} from '../entities/rfb-location/rfb-location.service';
import {ResponseWrapper} from '../shared/model/response-wrapper.model';
import {RfbLocation} from '../entities/rfb-location/rfb-location.model';
import {RfbEventService} from '../entities/rfb-event/rfb-event.service';

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

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private locationService: RfbLocationService,
        private eventService: RfbEventService
    ) {
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
        this.loadLocations();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', (message) => {
            this.principal.identity().then((account) => {
                this.account = account;
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
        // get selected location
        // get today's date
        // get event code
        // :: call event service and look for an event where those 3 items match
        // :: if you find a match log an eventAttendance for this user (this.account) and that event
    }

    clear() {}

    setHomeLocation() {}

}
