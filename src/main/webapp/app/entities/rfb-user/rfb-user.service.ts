import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import {SERVER_API_URL} from '../../app.constants';

import {RfbUser} from './rfb-user.model';
import {createRequestOption, ResponseWrapper} from '../../shared';

@Injectable()
export class RfbUserService {

    private resourceUrl = SERVER_API_URL + 'api/rfb-users';

    constructor(private http: Http) { }

    create(rfbUser: RfbUser): Observable<RfbUser> {
        const copy = this.convert(rfbUser);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(rfbUser: RfbUser): Observable<RfbUser> {
        const copy = this.convert(rfbUser);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<RfbUser> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(rfbUser: RfbUser): RfbUser {
        const copy: RfbUser = Object.assign({}, rfbUser);
        return copy;
    }
}
