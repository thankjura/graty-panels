alter table ${activeDoc:panel_renders} add column if not exists columns_config text;
alter table ${activeDoc:panel_renders} add column if not exists show_header boolean;
