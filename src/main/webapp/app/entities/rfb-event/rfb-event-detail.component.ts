import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiEventManager} from 'ng-jhipster';

import {RfbEvent} from './rfb-event.model';
import {RfbEventService} from './rfb-event.service';

@Component({
    selector: 'jhi-rfb-event-detail',
    templateUrl: './rfb-event-detail.component.html'
})
export class RfbEventDetailComponent implements OnInit, OnDestroy {

    rfbEvent: RfbEvent;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private rfbEventService: RfbEventService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInRfbEvents();
    }

    load(id) {
        this.rfbEventService.find(id).subscribe((rfbEvent) => {
            this.rfbEvent = rfbEvent;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInRfbEvents() {
        this.eventSubscriber = this.eventManager.subscribe(
            'rfbEventListModification',
            (response) => this.load(this.rfbEvent.id)
        );
    }
}
