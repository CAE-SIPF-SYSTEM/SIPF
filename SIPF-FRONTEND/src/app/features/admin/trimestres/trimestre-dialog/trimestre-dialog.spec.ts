import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TrimestreDialog } from './trimestre-dialog';

describe('TrimestreDialog', () => {
  let component: TrimestreDialog;
  let fixture: ComponentFixture<TrimestreDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TrimestreDialog],
    }).compileComponents();

    fixture = TestBed.createComponent(TrimestreDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
