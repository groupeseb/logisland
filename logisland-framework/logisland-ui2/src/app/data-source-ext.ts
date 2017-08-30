import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { DataSource } from '@angular/cdk';
import { Observable } from 'rxjs/Observable';

/**
 * Interface of a DataSource that can be filtered.
 *
 */
export abstract class DataSourceExt<T> extends DataSource<T> {
    _searchFilter = new BehaviorSubject('');
    get searchFilter(): string { return this._searchFilter.value; }
    set searchFilter(filter: string) { this._searchFilter.next(filter); }
}
