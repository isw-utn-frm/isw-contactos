export interface IContacto {
  id?: number;
  nombre?: string;
  celular?: number;
}

export class Contacto implements IContacto {
  constructor(public id?: number, public nombre?: string, public celular?: number) {}
}
