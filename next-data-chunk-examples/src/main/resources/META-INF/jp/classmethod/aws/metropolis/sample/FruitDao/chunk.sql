SELECT *
FROM fruits f

WHERE
    /*%if chunkable.idComparisonValue != null */
          AND f.fruit_id /*#chunkable.idComparisonOperator*/ /*chunkable.idComparisonValue*/'fruit_id_001'
    /*%end*/
ORDER BY f.fruit_id /*#chunkable.sortOrder*/

LIMIT /*chunkable.size*/10
