import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from 'rxjs/Rx';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

import {RfbUser} from './rfb-user.model';
import {RfbUserService} from './rfb-user.service';
import {Principal, ResponseWrapper} from '../../shared';

@Component({
    selector: 'jhi-rfb-user',
    templateUrl: './rfb-user.component.html'
})
export class RfbUserComponent implements OnInit, OnDestroy {
rfbUsers: RfbUser[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private rfbUserService: RfbUserService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.rfbUserService.query().subscribe(
            (res: ResponseWrapper) => {
                this.rfbUsers = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInRfbUsers();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: RfbUser) {
        return item.id;
    }
    registerChangeInRfbUsers() {
        this.eventSubscriber = this.eventManager.subscribe('rfbUserListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
