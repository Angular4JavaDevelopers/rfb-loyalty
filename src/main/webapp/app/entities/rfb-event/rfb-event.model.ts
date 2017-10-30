import {BaseEntity} from './../../shared';
import {RfbLocation} from '../rfb-location/rfb-location.model';

export class RfbEvent implements BaseEntity {
    constructor(
        public id?: number,
        public eventDate?: any,
        public eventCode?: string,
        public rfbLocationId?: number,
        public rfbLocationDTO?: RfbLocation,
        public rfbEventAttendances?: BaseEntity[],
    ) {
    }
}
