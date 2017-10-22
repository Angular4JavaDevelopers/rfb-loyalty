import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager} from 'ng-jhipster';

import {RfbLocation} from './rfb-location.model';
import {RfbLocationPopupService} from './rfb-location-popup.service';
import {RfbLocationService} from './rfb-location.service';

@Component({
    selector: 'jhi-rfb-location-delete-dialog',
    templateUrl: './rfb-location-delete-dialog.component.html'
})
export class RfbLocationDeleteDialogComponent {

    rfbLocation: RfbLocation;

    constructor(
        private rfbLocationService: RfbLocationService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.rfbLocationService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'rfbLocationListModification',
                content: 'Deleted a Location'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-rfb-location-delete-popup',
    template: ''
})
export class RfbLocationDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private rfbLocationPopupService: RfbLocationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.rfbLocationPopupService
                .open(RfbLocationDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
