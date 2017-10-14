import {BaseEntity} from './../../shared';

export class RfbEventAttendance implements BaseEntity {
    constructor(
        public id?: number,
        public attendanceDate?: any,
        public rfbEventId?: number,
        public rfbUserId?: number,
    ) {
    }
}
