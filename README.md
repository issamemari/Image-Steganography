# Image-Steganography

This is a simple tool for image steganography written in Kotlin. The tool allows embedding any given file within the least-significant bits of an image and extracting hidden files from images. Supported image formats are tiff, bmp, gif, wbmp, png.

We provide a command-line interface for the tool as a jar file (steg.jar). The tool can be executed with two subcommands: `embed` and `extract`.

```
Usage: embed [OPTIONS] INPUTIMAGEPATH OUTPUTIMAGEPATH

  Embed a file or a string in an image

Options:
  --number-of-bits INT      Number of least-significatn bits to use for
                            encoding the message
  --message-file-path TEXT  Path to the file to hide in the image
  --message TEXT            Message to encode
  -h, --help                Show this message and exit

Arguments:
  INPUTIMAGEPATH   Path to the input image
  OUTPUTIMAGEPATH  Path to the output image. Output image format must be tiff,
                   bmp, gif, wbmp, or png```

```
Usage: extract [OPTIONS] INPUTIMAGEPATH

  Extract a file or a string from an image

Options:`
  --output-directory-path TEXT  Path to the directory where the hidden file
                                will be output
  -h, --help                    Show this message and exit

Arguments:
  INPUTIMAGEPATH  Path to the input image
```
