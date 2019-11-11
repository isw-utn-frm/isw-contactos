export interface IPais {
  id?: number;
  nombre?: string;
  iso?: string;
}

export class Pais implements IPais {
  constructor(public id?: number, public nombre?: string, public iso?: string) {}
}
