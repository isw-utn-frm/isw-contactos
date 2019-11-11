import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TestTestModule } from '../../../test.module';
import { ContactoDetailComponent } from 'app/entities/contacto/contacto-detail.component';
import { Contacto } from 'app/shared/model/contacto.model';

describe('Component Tests', () => {
  describe('Contacto Management Detail Component', () => {
    let comp: ContactoDetailComponent;
    let fixture: ComponentFixture<ContactoDetailComponent>;
    const route = ({ data: of({ contacto: new Contacto(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TestTestModule],
        declarations: [ContactoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ContactoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContactoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.contacto).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
