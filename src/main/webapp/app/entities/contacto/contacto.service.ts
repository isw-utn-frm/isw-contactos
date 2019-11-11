import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IContacto } from 'app/shared/model/contacto.model';

type EntityResponseType = HttpResponse<IContacto>;
type EntityArrayResponseType = HttpResponse<IContacto[]>;

@Injectable({ providedIn: 'root' })
export class ContactoService {
  public resourceUrl = SERVER_API_URL + 'api/contactos';

  constructor(protected http: HttpClient) {}

  create(contacto: IContacto): Observable<EntityResponseType> {
    return this.http.post<IContacto>(this.resourceUrl, contacto, { observe: 'response' });
  }

  update(contacto: IContacto): Observable<EntityResponseType> {
    return this.http.put<IContacto>(this.resourceUrl, contacto, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContacto>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContacto[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
