import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SportDetailComponent } from './sport-detail.component';

describe('Component Tests', () => {
  describe('Sport Management Detail Component', () => {
    let comp: SportDetailComponent;
    let fixture: ComponentFixture<SportDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SportDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ sport: { idSport: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SportDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SportDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load sport on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.sport).toEqual(jasmine.objectContaining({ idSport: 123 }));
      });
    });
  });
});
