/**
 * This module supports color realted functions.
 */

%dw 2.0

/**
 * Maps a color object to a result color object.
 * @p data is an input color object.
 * @r a result color object.
 */
fun mapColor (data) = {
    name: data.color,
    "type": data.category,
    colorType: data."type",
    rgba: mapRgba(data.code.rgba),
    hex: data.code.hex
}

/**
 * Maps the provided RGBA array to a RGBA string.
 * @p rgba is an array with the 4 RGBA values.
 * @r A RGBA string.
 */
fun mapRgba (rgba) =
(if (!isEmpty(rgba))
    "(" ++ rgba[0] ++ ", " ++ rgba[1] ++ ", " ++ rgba[2] ++ ", " ++ rgba[3] ++ ")"
else null)