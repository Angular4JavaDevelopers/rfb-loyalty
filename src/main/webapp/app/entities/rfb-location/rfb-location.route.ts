import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes} from '@angular/router';

import {UserRouteAccessService} from '../../shared';
import {JhiPaginationUtil} from 'ng-jhipster';

import {RfbLocationComponent} from './rfb-location.component';
import {RfbLocationDetailComponent} from './rfb-location-detail.component';
import {RfbLocationPopupComponent} from './rfb-location-dialog.component';
import {RfbLocationDeletePopupComponent} from './rfb-location-delete-dialog.component';

@Injectable()
export class RfbLocationResolvePagingParams implements Resolve<any> {

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

export const rfbLocationRoute: Routes = [
    {
        path: 'rfb-location',
        component: RfbLocationComponent,
        resolve: {
            'pagingParams': RfbLocationResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'Locations'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'rfb-location/:id',
        component: RfbLocationDetailComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'Locations'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const rfbLocationPopupRoute: Routes = [
    {
        path: 'rfb-location-new',
        component: RfbLocationPopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'Locations'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'rfb-location/:id/edit',
        component: RfbLocationPopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'RfbLocations'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'rfb-location/:id/delete',
        component: RfbLocationDeletePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'Locations'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
