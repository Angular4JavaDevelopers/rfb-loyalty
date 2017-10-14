import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {RfbloyaltySharedModule} from '../../shared';
import {
    RfbEventAttendanceComponent,
    RfbEventAttendanceDeleteDialogComponent,
    RfbEventAttendanceDeletePopupComponent,
    RfbEventAttendanceDetailComponent,
    RfbEventAttendanceDialogComponent,
    RfbEventAttendancePopupComponent,
    rfbEventAttendancePopupRoute,
    RfbEventAttendancePopupService,
    rfbEventAttendanceRoute,
    RfbEventAttendanceService,
} from './';

const ENTITY_STATES = [
    ...rfbEventAttendanceRoute,
    ...rfbEventAttendancePopupRoute,
];

@NgModule({
    imports: [
        RfbloyaltySharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        RfbEventAttendanceComponent,
        RfbEventAttendanceDetailComponent,
        RfbEventAttendanceDialogComponent,
        RfbEventAttendanceDeleteDialogComponent,
        RfbEventAttendancePopupComponent,
        RfbEventAttendanceDeletePopupComponent,
    ],
    entryComponents: [
        RfbEventAttendanceComponent,
        RfbEventAttendanceDialogComponent,
        RfbEventAttendancePopupComponent,
        RfbEventAttendanceDeleteDialogComponent,
        RfbEventAttendanceDeletePopupComponent,
    ],
    providers: [
        RfbEventAttendanceService,
        RfbEventAttendancePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RfbloyaltyRfbEventAttendanceModule {}
