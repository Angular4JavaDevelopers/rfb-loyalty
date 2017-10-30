import {BaseEntity} from './../../shared';
import {RfbLocation} from '../rfb-location/rfb-location.model';

export class RfbUser implements BaseEntity {
    constructor(
        public id?: number,
        public username?: string,
        public rfbLocationDTO?: RfbLocation,
        public rfbEventAttendances?: BaseEntity[],
    ) {
    }
}
