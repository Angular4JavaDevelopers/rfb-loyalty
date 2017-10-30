import {BaseEntity} from './../../shared';
import {RfbEvent} from '../rfb-event/rfb-event.model';
import {RfbUser} from '../rfb-user/rfb-user.model';

export class RfbEventAttendance implements BaseEntity {
    constructor(
        public id?: number,
        public attendanceDate?: any,
        public rfbEventId?: number,
        public rfbUserId?: number,
        public rfbEventDTO?: RfbEvent,
        public rfbUserDTO?: RfbUser
    ) {
    }
}
