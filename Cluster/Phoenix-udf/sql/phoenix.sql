create table objectinfo(
      id char(32) not null primary key,
      name varchar,
      platformid varchar,
      tag varchar,
      pkey varchar,
      idcard varchar,
      sex integer,
      photo varbinary,
      feature float[],
      reason varchar,
      creator varchar,
      cphone varchar,
      createtime TIMESTAMP,
      updatetime TIMESTAMP,
      statustime TIMESTAMP,
      important integer,
      status integer,
      location varchar);

create table objectType(
      id char(32) not null primary key,
      name varchar,
      creator varchar,
      remark varchar,
      addtime TIMESTAMP,
      updatetime TIMESTAMP,
      ignore_region integer);