import { IGame } from 'app/shared/model/game.model';
import { IOrganizer } from 'app/shared/model/organizer.model';

export interface IInstance {
  id?: number;
  game?: IGame;
  organizers?: IOrganizer[];
}

export class Instance implements IInstance {
  constructor(public id?: number, public game?: IGame, public organizers?: IOrganizer[]) {}
}
