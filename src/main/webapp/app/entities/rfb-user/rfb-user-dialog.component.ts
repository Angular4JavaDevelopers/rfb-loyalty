import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

import {RfbUser} from './rfb-user.model';
import {RfbUserPopupService} from './rfb-user-popup.service';
import {RfbUserService} from './rfb-user.service';
import {RfbLocation, RfbLocationService} from '../rfb-location';
import {ResponseWrapper} from '../../shared';

@Component({
    selector: 'jhi-rfb-user-dialog',
    templateUrl: './rfb-user-dialog.component.html'
})
export class RfbUserDialogComponent implements OnInit {

    rfbUser: RfbUser;
    isSaving: boolean;

    homelocations: RfbLocation[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private rfbUserService: RfbUserService,
        private rfbLocationService: RfbLocationService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.rfbLocationService
            .query({filter: 'rfbuser-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.rfbUser.homeLocationId) {
                    this.homelocations = res.json;
                } else {
                    this.rfbLocationService
                        .find(this.rfbUser.homeLocationId)
                        .subscribe((subRes: RfbLocation) => {
                            this.homelocations = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.rfbUser.id !== undefined) {
            this.subscribeToSaveResponse(
                this.rfbUserService.update(this.rfbUser));
        } else {
            this.subscribeToSaveResponse(
                this.rfbUserService.create(this.rfbUser));
        }
    }

    private subscribeToSaveResponse(result: Observable<RfbUser>) {
        result.subscribe((res: RfbUser) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: RfbUser) {
        this.eventManager.broadcast({ name: 'rfbUserListModification', content: 'OK'});
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
    selector: 'jhi-rfb-user-popup',
    template: ''
})
export class RfbUserPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private rfbUserPopupService: RfbUserPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.rfbUserPopupService
                    .open(RfbUserDialogComponent as Component, params['id']);
            } else {
                this.rfbUserPopupService
                    .open(RfbUserDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
