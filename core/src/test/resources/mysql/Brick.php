<?php

namespace model;

/** This file is genereated, DO NOT EDIT */
class Brick
{

    public $id;
    public $identifier;
    public $beschreibung;
    public $flag_linkuse;

    public function __construct($id, $identifier, $beschreibung, $flag_linkuse)
    {
        $this->id = $id;
        $this->identifier = $identifier;
        $this->beschreibung = $beschreibung;
        $this->flag_linkuse = $flag_linkuse;
    }

    /** @var DB_PD $handle */
    public static function from_handle($handle)
    {
        $id = $handle->f("ID");
        $identifier = $handle->f("identifier");
        $beschreibung = $handle->f("beschreibung");
        $flag_linkuse = $handle->f("flag_linkuse");
        return new Brick($id, $identifier, $beschreibung, $flag_linkuse);
    }

}