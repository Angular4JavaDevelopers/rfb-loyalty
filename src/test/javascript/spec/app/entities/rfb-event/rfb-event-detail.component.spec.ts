/* tslint:disable max-line-length */
import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {OnInit} from '@angular/core';
import {DatePipe} from '@angular/common';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs/Rx';
import {JhiDataUtils, JhiDateUtils, JhiEventManager} from 'ng-jhipster';
import {RfbloyaltyTestModule} from '../../../test.module';
import {MockActivatedRoute} from '../../../helpers/mock-route.service';
import {RfbEventDetailComponent} from '../../../../../../main/webapp/app/entities/rfb-event/rfb-event-detail.component';
import {RfbEventService} from '../../../../../../main/webapp/app/entities/rfb-event/rfb-event.service';
import {RfbEvent} from '../../../../../../main/webapp/app/entities/rfb-event/rfb-event.model';

describe('Component Tests', () => {

    describe('RfbEvent Management Detail Component', () => {
        let comp: RfbEventDetailComponent;
        let fixture: ComponentFixture<RfbEventDetailComponent>;
        let service: RfbEventService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RfbloyaltyTestModule],
                declarations: [RfbEventDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    RfbEventService,
                    JhiEventManager
                ]
            }).overrideTemplate(RfbEventDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RfbEventDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RfbEventService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new RfbEvent(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.rfbEvent).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
