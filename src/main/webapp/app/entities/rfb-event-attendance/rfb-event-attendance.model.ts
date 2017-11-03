import {BaseEntity} from './../../shared';
import {RfbEvent} from '../rfb-event/rfb-event.model';
import {User} from '../../shared/user/user.model';

export class RfbEventAttendance implements BaseEntity {
    constructor(
        public id?: number,
        public attendanceDate?: any,
        public rfbEventDTO?: RfbEvent,
        public userDTO?: User
    ) {
    }
}
