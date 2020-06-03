<?php

namespace model;

/** This file is genereated, DO NOT EDIT */
class Brick
{

    public $id;
    public $identifier;
    public $beschreibung;
    public $linkuse;

    public function __construct($id, $identifier, $beschreibung, $linkuse)
    {
        $this->id = $id;
        $this->identifier = $identifier;
        $this->beschreibung = $beschreibung;
        $this->linkuse = $linkuse;
    }

    /** @var DB_PD $handle */
    public static function from_handle($handle)
    {
        $id = $handle->f("ID");
        $identifier = $handle->f("identifier");
        $beschreibung = $handle->f("beschreibung");
        $linkuse = $handle->f("flag_linkuse");
        return new Brick($id, $identifier, $beschreibung, $linkuse);
    }

}