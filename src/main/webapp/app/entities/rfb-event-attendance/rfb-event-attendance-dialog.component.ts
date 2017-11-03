import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

import {RfbEventAttendance} from './rfb-event-attendance.model';
import {RfbEventAttendancePopupService} from './rfb-event-attendance-popup.service';
import {RfbEventAttendanceService} from './rfb-event-attendance.service';
import {RfbEvent, RfbEventService} from '../rfb-event';
import {ResponseWrapper} from '../../shared';
import {User} from '../../shared/user/user.model';
import {UserService} from '../../shared/user/user.service';

@Component({
    selector: 'jhi-rfb-event-attendance-dialog',
    templateUrl: './rfb-event-attendance-dialog.component.html'
})
export class RfbEventAttendanceDialogComponent implements OnInit {

    rfbEventAttendance: RfbEventAttendance;
    isSaving: boolean;
    rfbevents: RfbEvent[];
    users: User[];
    attendanceDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private rfbEventAttendanceService: RfbEventAttendanceService,
        private rfbEventService: RfbEventService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.rfbEventService.query()
            .subscribe((res: ResponseWrapper) => { this.rfbevents = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.userService.findByAuthority('ROLE_RUNNER')
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        if (typeof this.rfbEventAttendance.rfbEventDTO === 'undefined') {
            this.rfbEventAttendance.rfbEventDTO = new RfbEvent();
            this.rfbEventAttendance.userDTO = new User();
        }
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.rfbEventAttendance.id !== undefined) {
            this.subscribeToSaveResponse(
                this.rfbEventAttendanceService.update(this.rfbEventAttendance));
        } else {
            this.subscribeToSaveResponse(
                this.rfbEventAttendanceService.create(this.rfbEventAttendance));
        }
    }

    private subscribeToSaveResponse(result: Observable<RfbEventAttendance>) {
        result.subscribe((res: RfbEventAttendance) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: RfbEventAttendance) {
        this.eventManager.broadcast({ name: 'rfbEventAttendanceListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.alertService.error(error.message, null, null);
    }

    trackRfbEventById(index: number, item: RfbEvent) {
        return item.id;
    }

    trackRfbUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-rfb-event-attendance-popup',
    template: ''
})
export class RfbEventAttendancePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private rfbEventAttendancePopupService: RfbEventAttendancePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.rfbEventAttendancePopupService
                    .open(RfbEventAttendanceDialogComponent as Component, params['id']);
            } else {
                this.rfbEventAttendancePopupService
                    .open(RfbEventAttendanceDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
