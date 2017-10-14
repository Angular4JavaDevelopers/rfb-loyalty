import {BaseEntity} from './../../shared';

export class RfbEvent implements BaseEntity {
    constructor(
        public id?: number,
        public eventDate?: any,
        public eventCode?: string,
        public rfbLocationId?: number,
        public rfbEventAttendances?: BaseEntity[],
    ) {
    }
}
