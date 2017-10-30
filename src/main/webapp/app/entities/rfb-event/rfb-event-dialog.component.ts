import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

import {RfbEvent} from './rfb-event.model';
import {RfbEventPopupService} from './rfb-event-popup.service';
import {RfbEventService} from './rfb-event.service';
import {RfbLocation, RfbLocationService} from '../rfb-location';
import {ResponseWrapper} from '../../shared';

@Component({
    selector: 'jhi-rfb-event-dialog',
    templateUrl: './rfb-event-dialog.component.html'
})
export class RfbEventDialogComponent implements OnInit {

    rfbEvent: RfbEvent;
    isSaving: boolean;

    rfblocations: RfbLocation[];
    eventDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private rfbEventService: RfbEventService,
        private rfbLocationService: RfbLocationService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.rfbLocationService.query()
            .subscribe((res: ResponseWrapper) => {
                    this.rfblocations = res.json;
                },
                (res: ResponseWrapper) => this.onError(res.json)
            );
        if (typeof this.rfbEvent.rfbLocationDTO === 'undefined') {
            this.rfbEvent.rfbLocationDTO = new RfbLocation();
        }
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.rfbEvent.id !== undefined) {
            this.subscribeToSaveResponse(
                this.rfbEventService.update(this.rfbEvent));
        } else {
            this.subscribeToSaveResponse(
                this.rfbEventService.create(this.rfbEvent));
        }
    }

    private subscribeToSaveResponse(result: Observable<RfbEvent>) {
        result.subscribe((res: RfbEvent) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: RfbEvent) {
        this.eventManager.broadcast({ name: 'rfbEventListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.alertService.error(error.message, null, null);
    }

    trackRfbLocationById(index: number, item: RfbLocation) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-rfb-event-popup',
    template: ''
})
export class RfbEventPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private rfbEventPopupService: RfbEventPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.rfbEventPopupService
                    .open(RfbEventDialogComponent as Component, params['id']);
            } else {
                this.rfbEventPopupService
                    .open(RfbEventDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
