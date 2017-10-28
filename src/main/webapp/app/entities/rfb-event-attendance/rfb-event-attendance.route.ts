import {Routes} from '@angular/router';

import {UserRouteAccessService} from '../../shared';

import {RfbEventAttendanceComponent} from './rfb-event-attendance.component';
import {RfbEventAttendanceDetailComponent} from './rfb-event-attendance-detail.component';
import {RfbEventAttendancePopupComponent} from './rfb-event-attendance-dialog.component';
import {RfbEventAttendanceDeletePopupComponent} from './rfb-event-attendance-delete-dialog.component';

export const rfbEventAttendanceRoute: Routes = [
    {
        path: 'rfb-event-attendance',
        component: RfbEventAttendanceComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'Event Attendances'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'rfb-event-attendance/:id',
        component: RfbEventAttendanceDetailComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'Event Attendances'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const rfbEventAttendancePopupRoute: Routes = [
    {
        path: 'rfb-event-attendance-new',
        component: RfbEventAttendancePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'Event Attendances'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'rfb-event-attendance/:id/edit',
        component: RfbEventAttendancePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'Event Attendances'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'rfb-event-attendance/:id/delete',
        component: RfbEventAttendanceDeletePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'Event Attendances'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
