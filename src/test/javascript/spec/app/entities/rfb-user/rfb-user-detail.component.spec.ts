/* tslint:disable max-line-length */
import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {OnInit} from '@angular/core';
import {DatePipe} from '@angular/common';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs/Rx';
import {JhiDataUtils, JhiDateUtils, JhiEventManager} from 'ng-jhipster';
import {RfbloyaltyTestModule} from '../../../test.module';
import {MockActivatedRoute} from '../../../helpers/mock-route.service';
import {RfbUserDetailComponent} from '../../../../../../main/webapp/app/entities/rfb-user/rfb-user-detail.component';
import {RfbUserService} from '../../../../../../main/webapp/app/entities/rfb-user/rfb-user.service';
import {RfbUser} from '../../../../../../main/webapp/app/entities/rfb-user/rfb-user.model';

describe('Component Tests', () => {

    describe('RfbUser Management Detail Component', () => {
        let comp: RfbUserDetailComponent;
        let fixture: ComponentFixture<RfbUserDetailComponent>;
        let service: RfbUserService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RfbloyaltyTestModule],
                declarations: [RfbUserDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    RfbUserService,
                    JhiEventManager
                ]
            }).overrideTemplate(RfbUserDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RfbUserDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RfbUserService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new RfbUser(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.rfbUser).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
