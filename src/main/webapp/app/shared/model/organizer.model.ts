import { IGame } from 'app/shared/model/game.model';
import { IInstance } from 'app/shared/model/instance.model';
import { IRuleset } from 'app/shared/model/ruleset.model';

export interface IOrganizer {
  id?: number;
  games?: IGame[];
  instances?: IInstance[];
  rulesets?: IRuleset[];
}

export class Organizer implements IOrganizer {
  constructor(public id?: number, public games?: IGame[], public instances?: IInstance[], public rulesets?: IRuleset[]) {}
}
