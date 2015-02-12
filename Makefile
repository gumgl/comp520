.PHONY: main package clean

main:
	make -C src

clean:
	make --ignore-errors -C src clean
	if [ -f william_bain.tar.gz ]; then rm william_bain.tar.gz; fi
	if [ -d temp ]; then rm -r temp; fi

package:
	make clean
	mkdir temp temp/william_bain temp/william_bain/programs
	cp -r README src run temp/william_bain
	cp -r programs/invalidtype temp/william_bain/programs
	rm temp/william_bain/programs/invalidtype/assign_float_literal_to_int_variable.min
	rm temp/william_bain/programs/invalidtype/duplicate_declaration.min
	cd temp; tar -cf ../william_bain.tar william_bain
	gzip william_bain.tar
	rm -r temp
