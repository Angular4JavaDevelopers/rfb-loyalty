import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager} from 'ng-jhipster';

import {RfbEvent} from './rfb-event.model';
import {RfbEventPopupService} from './rfb-event-popup.service';
import {RfbEventService} from './rfb-event.service';

@Component({
    selector: 'jhi-rfb-event-delete-dialog',
    templateUrl: './rfb-event-delete-dialog.component.html'
})
export class RfbEventDeleteDialogComponent {

    rfbEvent: RfbEvent;

    constructor(
        private rfbEventService: RfbEventService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.rfbEventService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'rfbEventListModification',
                content: 'Deleted an Event'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-rfb-event-delete-popup',
    template: ''
})
export class RfbEventDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private rfbEventPopupService: RfbEventPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.rfbEventPopupService
                .open(RfbEventDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
