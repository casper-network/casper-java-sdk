import { Component, OnInit } from '@angular/core';
import { Block } from './block.model';
import { BlockService } from './block.service';

@Component({
  selector: 'app-block',
  templateUrl: './block.component.html',
  styleUrls: ['./block.component.css']
})
export class BlockComponent implements OnInit {

  constructor(private blockService: BlockService) { }

  ngOnInit(): void {
    this.blockService.getBlock().subscribe((block: Block) => { console.log(block.api_version); });
  }

}
