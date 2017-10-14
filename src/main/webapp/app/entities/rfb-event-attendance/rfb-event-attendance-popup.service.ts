import {Component, Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {RfbEventAttendance} from './rfb-event-attendance.model';
import {RfbEventAttendanceService} from './rfb-event-attendance.service';

@Injectable()
export class RfbEventAttendancePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private rfbEventAttendanceService: RfbEventAttendanceService

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
                this.rfbEventAttendanceService.find(id).subscribe((rfbEventAttendance) => {
                    if (rfbEventAttendance.attendanceDate) {
                        rfbEventAttendance.attendanceDate = {
                            year: rfbEventAttendance.attendanceDate.getFullYear(),
                            month: rfbEventAttendance.attendanceDate.getMonth() + 1,
                            day: rfbEventAttendance.attendanceDate.getDate()
                        };
                    }
                    this.ngbModalRef = this.rfbEventAttendanceModalRef(component, rfbEventAttendance);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.rfbEventAttendanceModalRef(component, new RfbEventAttendance());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    rfbEventAttendanceModalRef(component: Component, rfbEventAttendance: RfbEventAttendance): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.rfbEventAttendance = rfbEventAttendance;
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
