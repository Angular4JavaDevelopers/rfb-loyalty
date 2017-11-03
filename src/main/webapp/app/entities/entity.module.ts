import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';

import {RfbloyaltyRfbLocationModule} from './rfb-location/rfb-location.module';
import {RfbloyaltyRfbEventModule} from './rfb-event/rfb-event.module';
import {RfbloyaltyRfbEventAttendanceModule} from './rfb-event-attendance/rfb-event-attendance.module';

/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        RfbloyaltyRfbLocationModule,
        RfbloyaltyRfbEventModule,
        RfbloyaltyRfbEventAttendanceModule
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RfbloyaltyEntityModule {}
