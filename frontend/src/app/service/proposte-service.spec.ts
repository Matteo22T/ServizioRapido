import { TestBed } from '@angular/core/testing';

import { ProposteService } from './proposte-service';

describe('ProposteService', () => {
  let service: ProposteService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProposteService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
