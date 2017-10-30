import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes} from '@angular/router';

import {UserRouteAccessService} from '../../shared';
import {JhiPaginationUtil} from 'ng-jhipster';

import {RfbEventComponent} from './rfb-event.component';
import {RfbEventDetailComponent} from './rfb-event-detail.component';
import {RfbEventPopupComponent} from './rfb-event-dialog.component';
import {RfbEventDeletePopupComponent} from './rfb-event-delete-dialog.component';

@Injectable()
export class RfbEventResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const rfbEventRoute: Routes = [
    {
        path: 'rfb-event',
        component: RfbEventComponent,
        resolve: {
            'pagingParams': RfbEventResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ORGANIZER', 'ROLE_ADMIN'],
            pageTitle: 'Events'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'rfb-event/:id',
        component: RfbEventDetailComponent,
        data: {
            authorities: ['ROLE_ORGANIZER', 'ROLE_ADMIN'],
            pageTitle: 'Events'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const rfbEventPopupRoute: Routes = [
    {
        path: 'rfb-event-new',
        component: RfbEventPopupComponent,
        data: {
            authorities: ['ROLE_ORGANIZER', 'ROLE_ADMIN'],
            pageTitle: 'Events'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'rfb-event/:id/edit',
        component: RfbEventPopupComponent,
        data: {
            authorities: ['ROLE_ORGANIZER', 'ROLE_ADMIN'],
            pageTitle: 'Events'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'rfb-event/:id/delete',
        component: RfbEventDeletePopupComponent,
        data: {
            authorities: ['ROLE_ORGANIZER', 'ROLE_ADMIN'],
            pageTitle: 'Events'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
