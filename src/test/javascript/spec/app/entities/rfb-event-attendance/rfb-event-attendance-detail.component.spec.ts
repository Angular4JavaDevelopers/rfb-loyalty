/* tslint:disable max-line-length */
import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {OnInit} from '@angular/core';
import {DatePipe} from '@angular/common';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs/Rx';
import {JhiDataUtils, JhiDateUtils, JhiEventManager} from 'ng-jhipster';
import {RfbloyaltyTestModule} from '../../../test.module';
import {MockActivatedRoute} from '../../../helpers/mock-route.service';
import {RfbEventAttendanceDetailComponent} from '../../../../../../main/webapp/app/entities/rfb-event-attendance/rfb-event-attendance-detail.component';
import {RfbEventAttendanceService} from '../../../../../../main/webapp/app/entities/rfb-event-attendance/rfb-event-attendance.service';
import {RfbEventAttendance} from '../../../../../../main/webapp/app/entities/rfb-event-attendance/rfb-event-attendance.model';

describe('Component Tests', () => {

    describe('RfbEventAttendance Management Detail Component', () => {
        let comp: RfbEventAttendanceDetailComponent;
        let fixture: ComponentFixture<RfbEventAttendanceDetailComponent>;
        let service: RfbEventAttendanceService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RfbloyaltyTestModule],
                declarations: [RfbEventAttendanceDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    RfbEventAttendanceService,
                    JhiEventManager
                ]
            }).overrideTemplate(RfbEventAttendanceDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RfbEventAttendanceDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RfbEventAttendanceService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new RfbEventAttendance(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.rfbEventAttendance).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
