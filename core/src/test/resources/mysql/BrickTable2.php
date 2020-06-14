<?php

namespace tablemodel\tables;

use tablemodel\Table;

/** This file is generated, DO NOT EDIT */
class BrickTable extends Table
{

    public function __construct()
    {
        parent::__construct("tbrick", "Brick");
        $this->addRow("ID");
        $this->addRow("identifier");
        $this->addRow("beschreibung");
        $this->addAliasRow("flag_linkuse", "linkuse");
    }

}