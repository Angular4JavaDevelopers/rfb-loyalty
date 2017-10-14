/* tslint:disable max-line-length */
import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {OnInit} from '@angular/core';
import {DatePipe} from '@angular/common';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs/Rx';
import {JhiDataUtils, JhiDateUtils, JhiEventManager} from 'ng-jhipster';
import {RfbloyaltyTestModule} from '../../../test.module';
import {MockActivatedRoute} from '../../../helpers/mock-route.service';
import {RfbLocationDetailComponent} from '../../../../../../main/webapp/app/entities/rfb-location/rfb-location-detail.component';
import {RfbLocationService} from '../../../../../../main/webapp/app/entities/rfb-location/rfb-location.service';
import {RfbLocation} from '../../../../../../main/webapp/app/entities/rfb-location/rfb-location.model';

describe('Component Tests', () => {

    describe('RfbLocation Management Detail Component', () => {
        let comp: RfbLocationDetailComponent;
        let fixture: ComponentFixture<RfbLocationDetailComponent>;
        let service: RfbLocationService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RfbloyaltyTestModule],
                declarations: [RfbLocationDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    RfbLocationService,
                    JhiEventManager
                ]
            }).overrideTemplate(RfbLocationDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RfbLocationDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RfbLocationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new RfbLocation(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.rfbLocation).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
