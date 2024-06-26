#!/usr/bin/python
#  pylint: disable=missing-module-docstring

import sys
from spacewalk.server import rhnSQL
from spacewalk.server.importlib import backendOracle
from spacewalk.server.importlib.backendLib import DBstring

exitval = 0

rhnSQL.initDB()
q = rhnSQL.prepare(
    """select data_length
                        from user_tab_columns
                       where upper(table_name) = upper(:tname)
                         and upper(column_name) = upper(:cname)"""
)

backend = backendOracle.PostgresqlBackend()

for tn, tc in list(backend.tables.items()):
    for cn, cv in list(tc.getFields().items()):
        if isinstance(cv, DBstring):
            q.execute(tname=tn, cname=cn)
            row = q.fetchone_dict()
            if not row or row["data_length"] != cv.limit:
                print(
                    (
                        (
                            "ERROR: database column %s.%s is %s chars long "
                            + "but defined as %s chars in backendOracle.py"
                        )
                        % (tn, cn, row["data_length"], cv.limit)
                    )
                )
                exitval = 1
            else:
                # pylint: disable-next=consider-using-f-string
                print(("%s.%s = %d" % (tn, cn, row["data_length"])))

sys.exit(exitval)
