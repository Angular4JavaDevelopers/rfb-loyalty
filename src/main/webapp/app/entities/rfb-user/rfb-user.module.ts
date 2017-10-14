import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {RfbloyaltySharedModule} from '../../shared';
import {
    RfbUserComponent,
    RfbUserDeleteDialogComponent,
    RfbUserDeletePopupComponent,
    RfbUserDetailComponent,
    RfbUserDialogComponent,
    RfbUserPopupComponent,
    rfbUserPopupRoute,
    RfbUserPopupService,
    rfbUserRoute,
    RfbUserService,
} from './';

const ENTITY_STATES = [
    ...rfbUserRoute,
    ...rfbUserPopupRoute,
];

@NgModule({
    imports: [
        RfbloyaltySharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        RfbUserComponent,
        RfbUserDetailComponent,
        RfbUserDialogComponent,
        RfbUserDeleteDialogComponent,
        RfbUserPopupComponent,
        RfbUserDeletePopupComponent,
    ],
    entryComponents: [
        RfbUserComponent,
        RfbUserDialogComponent,
        RfbUserPopupComponent,
        RfbUserDeleteDialogComponent,
        RfbUserDeletePopupComponent,
    ],
    providers: [
        RfbUserService,
        RfbUserPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RfbloyaltyRfbUserModule {}
