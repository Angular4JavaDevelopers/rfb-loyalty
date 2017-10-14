import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {RfbloyaltySharedModule} from '../../shared';
import {
    RfbEventComponent,
    RfbEventDeleteDialogComponent,
    RfbEventDeletePopupComponent,
    RfbEventDetailComponent,
    RfbEventDialogComponent,
    RfbEventPopupComponent,
    rfbEventPopupRoute,
    RfbEventPopupService,
    RfbEventResolvePagingParams,
    rfbEventRoute,
    RfbEventService,
} from './';

const ENTITY_STATES = [
    ...rfbEventRoute,
    ...rfbEventPopupRoute,
];

@NgModule({
    imports: [
        RfbloyaltySharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        RfbEventComponent,
        RfbEventDetailComponent,
        RfbEventDialogComponent,
        RfbEventDeleteDialogComponent,
        RfbEventPopupComponent,
        RfbEventDeletePopupComponent,
    ],
    entryComponents: [
        RfbEventComponent,
        RfbEventDialogComponent,
        RfbEventPopupComponent,
        RfbEventDeleteDialogComponent,
        RfbEventDeletePopupComponent,
    ],
    providers: [
        RfbEventService,
        RfbEventPopupService,
        RfbEventResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RfbloyaltyRfbEventModule {}
