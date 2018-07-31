paste strings.txt imagein.txt imageout.txt | (
while read string imgIN imgOUT ; do
java -jar /home/issa/IdeaProjects/Image-Steganography/target/ImageSteganography-1.0-jar-with-dependencies.jar embed --number-of-bits=1 ${string} ${imgIN} ${imgOUT}
done
)
