import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager} from 'ng-jhipster';

import {RfbEventAttendance} from './rfb-event-attendance.model';
import {RfbEventAttendancePopupService} from './rfb-event-attendance-popup.service';
import {RfbEventAttendanceService} from './rfb-event-attendance.service';

@Component({
    selector: 'jhi-rfb-event-attendance-delete-dialog',
    templateUrl: './rfb-event-attendance-delete-dialog.component.html'
})
export class RfbEventAttendanceDeleteDialogComponent {

    rfbEventAttendance: RfbEventAttendance;

    constructor(
        private rfbEventAttendanceService: RfbEventAttendanceService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.rfbEventAttendanceService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'rfbEventAttendanceListModification',
                content: 'Deleted an Event Attendance'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-rfb-event-attendance-delete-popup',
    template: ''
})
export class RfbEventAttendanceDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private rfbEventAttendancePopupService: RfbEventAttendancePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.rfbEventAttendancePopupService
                .open(RfbEventAttendanceDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
