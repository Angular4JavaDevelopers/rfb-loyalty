import { Route } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { SettingsComponent } from './settings.component';

export const settingsRoute: Route = {
    path: 'settings',
    component: SettingsComponent,
    data: {
        authorities: ['ROLE_RUNNER', 'ROLE_ORGANIZER', 'ROLE_ADMIN'],
        pageTitle: 'Settings'
    },
    canActivate: [UserRouteAccessService]
};
