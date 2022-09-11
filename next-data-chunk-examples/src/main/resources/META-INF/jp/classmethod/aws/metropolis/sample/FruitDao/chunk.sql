SELECT *
FROM fruits f

WHERE
    /*%if chunkable.after != null */
        /*%if "DESC".equals(chunkable.direction) == false */
            AND f.fruit_id > /*chunkable.after*/'fruit_id_001'
        /*%else*/
            AND f.fruit_id < /*chunkable.after*/'fruit_id_001'
        /*%end*/
    /*%end*/

    /*%if chunkable.before != null */
        /*%if "DESC".equals(chunkable.direction) == false */
            AND f.fruit_id < /*chunkable.before*/'fruit_id_001'
        /*%else*/
            AND f.fruit_id > /*chunkable.before*/'fruit_id_001'
        /*%end*/
    /*%end*/

ORDER BY f.fruit_id
    /*%if chunkable.direction == null || "ASC".equals(chunkable.direction) */
        /*%if chunkable.before == null */
            ASC
        /*%else*/
            DESC
        /*%end*/
    /*%end*/

    /*%if "DESC".equals(chunkable.direction) */
        /*%if chunkable.before != null */
            ASC
        /*%else*/
            DESC
        /*%end*/
    /*%end*/

LIMIT
/*%if chunkable.size != null */
    /*chunkable.size*/10
/*%end*/
