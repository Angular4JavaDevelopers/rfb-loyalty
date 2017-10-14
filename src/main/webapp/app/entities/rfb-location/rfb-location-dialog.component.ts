import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

import {RfbLocation} from './rfb-location.model';
import {RfbLocationPopupService} from './rfb-location-popup.service';
import {RfbLocationService} from './rfb-location.service';

@Component({
    selector: 'jhi-rfb-location-dialog',
    templateUrl: './rfb-location-dialog.component.html'
})
export class RfbLocationDialogComponent implements OnInit {

    rfbLocation: RfbLocation;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private rfbLocationService: RfbLocationService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.rfbLocation.id !== undefined) {
            this.subscribeToSaveResponse(
                this.rfbLocationService.update(this.rfbLocation));
        } else {
            this.subscribeToSaveResponse(
                this.rfbLocationService.create(this.rfbLocation));
        }
    }

    private subscribeToSaveResponse(result: Observable<RfbLocation>) {
        result.subscribe((res: RfbLocation) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: RfbLocation) {
        this.eventManager.broadcast({ name: 'rfbLocationListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.alertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-rfb-location-popup',
    template: ''
})
export class RfbLocationPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private rfbLocationPopupService: RfbLocationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.rfbLocationPopupService
                    .open(RfbLocationDialogComponent as Component, params['id']);
            } else {
                this.rfbLocationPopupService
                    .open(RfbLocationDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
