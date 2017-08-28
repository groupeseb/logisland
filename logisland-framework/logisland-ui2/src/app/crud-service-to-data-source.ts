import { DataSource } from '@angular/cdk';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Observable } from 'rxjs/Observable';
import { CrudService } from './crud-service';

/**
 * Convert a CrudService to a DataSource
 */
export class CrudServiceToDataSource<T> extends DataSource<T> {
    _filterChange = new BehaviorSubject('');
    get filter(): string { return this._filterChange.value; }
    set filter(filter: string) { this._filterChange.next(filter); }

    constructor(private _crudService: CrudService<T>) {
      super();
    }

    connect(): Observable<T[]> {
        return this._crudService.query().$observable;
    }

    disconnect() { }
  }
