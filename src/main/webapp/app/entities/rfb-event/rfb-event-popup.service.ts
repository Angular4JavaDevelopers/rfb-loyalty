import {Component, Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {RfbEvent} from './rfb-event.model';
import {RfbEventService} from './rfb-event.service';

@Injectable()
export class RfbEventPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private rfbEventService: RfbEventService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.rfbEventService.find(id).subscribe((rfbEvent) => {
                    if (rfbEvent.eventDate) {
                        rfbEvent.eventDate = {
                            year: rfbEvent.eventDate.getFullYear(),
                            month: rfbEvent.eventDate.getMonth() + 1,
                            day: rfbEvent.eventDate.getDate()
                        };
                    }
                    this.ngbModalRef = this.rfbEventModalRef(component, rfbEvent);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.rfbEventModalRef(component, new RfbEvent());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    rfbEventModalRef(component: Component, rfbEvent: RfbEvent): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.rfbEvent = rfbEvent;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
