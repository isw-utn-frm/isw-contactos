import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Contacto } from 'app/shared/model/contacto.model';
import { ContactoService } from './contacto.service';
import { ContactoComponent } from './contacto.component';
import { ContactoDetailComponent } from './contacto-detail.component';
import { ContactoUpdateComponent } from './contacto-update.component';
import { ContactoDeletePopupComponent } from './contacto-delete-dialog.component';
import { IContacto } from 'app/shared/model/contacto.model';

@Injectable({ providedIn: 'root' })
export class ContactoResolve implements Resolve<IContacto> {
  constructor(private service: ContactoService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContacto> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((contacto: HttpResponse<Contacto>) => contacto.body));
    }
    return of(new Contacto());
  }
}

export const contactoRoute: Routes = [
  {
    path: '',
    component: ContactoComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Contactos'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ContactoDetailComponent,
    resolve: {
      contacto: ContactoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Contactos'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ContactoUpdateComponent,
    resolve: {
      contacto: ContactoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Contactos'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ContactoUpdateComponent,
    resolve: {
      contacto: ContactoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Contactos'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const contactoPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ContactoDeletePopupComponent,
    resolve: {
      contacto: ContactoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Contactos'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
