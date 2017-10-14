import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiEventManager} from 'ng-jhipster';

import {RfbEventAttendance} from './rfb-event-attendance.model';
import {RfbEventAttendanceService} from './rfb-event-attendance.service';

@Component({
    selector: 'jhi-rfb-event-attendance-detail',
    templateUrl: './rfb-event-attendance-detail.component.html'
})
export class RfbEventAttendanceDetailComponent implements OnInit, OnDestroy {

    rfbEventAttendance: RfbEventAttendance;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private rfbEventAttendanceService: RfbEventAttendanceService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInRfbEventAttendances();
    }

    load(id) {
        this.rfbEventAttendanceService.find(id).subscribe((rfbEventAttendance) => {
            this.rfbEventAttendance = rfbEventAttendance;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInRfbEventAttendances() {
        this.eventSubscriber = this.eventManager.subscribe(
            'rfbEventAttendanceListModification',
            (response) => this.load(this.rfbEventAttendance.id)
        );
    }
}
